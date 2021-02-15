package com.khovaylo.surf.dto.converter;

/**
 * @author Pavel Khovaylo
 */
public interface Converter<D, M> {

    M toModel(D dto);

    D toDto(M model);
}
