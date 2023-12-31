package com.muhammetkdr.pokemondex.data.repository

import com.muhammetkdr.pokemondex.common.mapTo
import com.muhammetkdr.pokemondex.common.networkresponse.NetworkResponse
import com.muhammetkdr.pokemondex.common.toPokemonEntity
import com.muhammetkdr.pokemondex.common.toPokemonListEntity
import com.muhammetkdr.pokemondex.common.toPokemonSpeciesEntity
import com.muhammetkdr.pokemondex.data.source.PokeRemoteDataSource
import com.muhammetkdr.pokemondex.common.di.Dispatcher
import com.muhammetkdr.pokemondex.common.di.DispatcherType
import com.muhammetkdr.pokemondex.domain.model.PokemonEntity
import com.muhammetkdr.pokemondex.domain.model.PokemonListEntity
import com.muhammetkdr.pokemondex.domain.model.PokemonSpeciesEntity
import com.muhammetkdr.pokemondex.domain.repository.PokeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokeRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokeRemoteDataSource,
    @Dispatcher(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : PokeRepository {

    override fun getPokemonList(
        limit: Int,
        offset: Int
    ): Flow<NetworkResponse<List<PokemonListEntity>>> {
        return flow {
            emit(NetworkResponse.Loading)
            val response = remoteDataSource.getPokemonList(limit, offset)
            emit(NetworkResponse.Success(response.mapTo { it.toPokemonListEntity() }))
        }.catch {
            emit(NetworkResponse.Error(it.message.orEmpty()))
        }.flowOn(ioDispatcher)
    }

    override fun getPokemonInfo(name: String): Flow<NetworkResponse<PokemonEntity>> {
        return flow {
            emit(NetworkResponse.Loading)
            val response = remoteDataSource.getPokemonInfo(name)
            emit(NetworkResponse.Success(response.mapTo { it.toPokemonEntity() }))
        }.catch {
            emit(NetworkResponse.Error(it.message.orEmpty()))
        }.flowOn(ioDispatcher)
    }

    override fun getPokemonSpecies(name: String): Flow<NetworkResponse<PokemonSpeciesEntity>> {
        return flow {
            emit(NetworkResponse.Loading)
            val response = remoteDataSource.getPokemonSpecies(name)
            emit(NetworkResponse.Success(response.mapTo { it.toPokemonSpeciesEntity() }))
        }.catch {
            emit(NetworkResponse.Error(it.message.orEmpty()))
        }.flowOn(ioDispatcher)
    }
}