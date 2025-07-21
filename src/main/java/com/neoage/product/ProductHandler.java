package com.neoage.product;

import com.neoage.constant.AppConstant;
import com.neoage.constant.ErrorCode;
import com.neoage.exception.ProductManagementException;
import com.neoage.validator.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductHandler {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                //.contentType(MediaType.TEXT_EVENT_STREAM) //http GET http://localhost:8080/api/v1/products
                .contentType(MediaType.APPLICATION_NDJSON) //http --stream GET http://localhost:8080/api/v1/products
                .body(productRepository.findAll().map(productMapper::toProductDto).delayElements(Duration.ofSeconds(1)), Product.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        final var id = getId(serverRequest);
        return productRepository.findById(id)
                .map(productMapper::toProductDto)
                .flatMap(ServerResponse.ok()::bodyValue)
                .switchIfEmpty(Mono.error(new ProductManagementException(ErrorCode.PRODUCT_NOT_FOUND)))
                .doOnError(ex -> log.error("Error while fetching product with id {}: {}", id, ex.getMessage()));
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductDto.class)
                .flatMap(validatorUtil::validate)
                .map(productMapper::toProduct)
                .map(product -> {
                    product.setId(UUID.randomUUID());
                    return product;
                })
                .flatMap(productRepository::save)
                .map(productMapper::toProductDto)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        final var id = getId(serverRequest);
        final var productDtoMono = serverRequest.bodyToMono(ProductDto.class).flatMap(validatorUtil::validate);
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductManagementException(ErrorCode.PRODUCT_NOT_FOUND)))
                .doOnError(ex -> log.error("Error while updating product with id {}: {}", id, ex.getMessage()))
                .zipWith(productDtoMono)
                .map(tuple -> productMapper.mergeToProduct(tuple.getT2(), tuple.getT1()))
                .flatMap(productRepository::save)
                .map(productMapper::toProductDto)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        final var id = getId(serverRequest);
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductManagementException(ErrorCode.PRODUCT_NOT_FOUND)))
                .doOnError(ex -> log.error("Error while deleting product with id {}: {}", id, ex.getMessage()))
                .flatMap(productRepository::delete)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteAll(ServerRequest serverRequest) {
        return productRepository.deleteAll()
                .then(ServerResponse.noContent().build());
    }

    private UUID getId(ServerRequest serverRequest) {
        try {
            return UUID.fromString(serverRequest.pathVariable(AppConstant.ID));
        } catch (IllegalArgumentException ex) {
            log.error("Error while getting UUID from id {}: {}", serverRequest.pathVariable(AppConstant.ID), ex.getMessage());
            throw new ProductManagementException(ErrorCode.INVALID_UUID_FORMAT);
        }
    }
}
