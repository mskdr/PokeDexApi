package com.muhammetkdr.pokemondex.domain.usecase

import com.muhammetkdr.pokemondex.domain.repository.PokeRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(private val pokeRepository: PokeRepository) {
    operator fun invoke() = pokeRepository.getPokemonList(201,0)
}