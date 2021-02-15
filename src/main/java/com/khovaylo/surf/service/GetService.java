package com.khovaylo.surf.service;

/**
 * @author Pavel Khovaylo
 */
public interface GetService<T, M> {
    M get(T id);
}
