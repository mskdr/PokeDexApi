package com.muhammetkdr.pokemondex.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muhammetkdr.pokemondex.ui.base.BaseViewModel
import com.muhammetkdr.pokemondex.common.capitalizeWords
import com.muhammetkdr.pokemondex.common.networkresponse.NetworkResponse
import com.muhammetkdr.pokemondex.domain.usecase.GetPokemonInfoUseCase
import com.muhammetkdr.pokemondex.domain.usecase.GetPokemonSpeciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase,
    private val getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase
) : BaseViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonUiState>()
    val pokemonDetail: LiveData<PokemonUiState> = _pokemonDetail

    private val _pokemonSpecies = MutableLiveData<PokemonSpeciesUiState>()
    val pokemonStateSpecies: LiveData<PokemonSpeciesUiState> = _pokemonSpecies

    fun getPokemon(name: String) = viewModelScope.launch{

        getPokemonInfoUseCase(name = name.lowercase())
            .onStart { showIndicator() }
            .collect { pokemonEntity ->
            when (pokemonEntity) {
                is NetworkResponse.Error -> {
                    val state = PokemonUiState(
                        isError = true,
                        errorMessage = pokemonEntity.error
                    )
                    _pokemonDetail.postValue(state)
                }

                NetworkResponse.Loading -> Unit // loading state has been handled by base structure

                is NetworkResponse.Success -> {

                    val pokeUiState = pokemonEntity.data.run {
                        PokemonUiState(
                            pokeUuid = pokemonUuid,
                            pokeName = pokeName.capitalizeWords(),
                            pokeId = (pokemonUuid).getPokemonId(),
                            pokeImgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonUuid}.png",
                            isFirst = pokemonUuid == firstItem,
                            isLast = pokemonUuid == lastItem,
                            height = height,
                            weight = weight,
                            moves = moves,
                            pokeTypes = pokeTypes,
                            hpStat = hpStat,
                            hpStatProgress = hpStatProgress,
                            attackStat = attackStat,
                            attackStatProgress = attackStatProgress,
                            defenseStat = defenseStat,
                            defenseStatProgress = defenseStatProgress,
                            specialAttackStat = specialAttackStat,
                            specialAttackStatProgress = specialAttackStatProgress,
                            specialDefenseStat = specialDefenseStat,
                            specialDefenseStatProgress = specialDefenseStatProgress,
                            speedStat = speedStat,
                            speedStatProgress = speedStatProgress,
                            pokemonColor = pokemonColor
                        )
                    }

                    _pokemonDetail.postValue(pokeUiState)
                }
            }
        }

        getPokemonSpeciesUseCase(name = name.lowercase())
            .onCompletion { hideIndicator() }
            .collect { pokemonSpeciesEntity ->

            when (pokemonSpeciesEntity) {
                is NetworkResponse.Error -> {
                    val state = PokemonSpeciesUiState(
                        isError = true,
                        errorMessage = pokemonSpeciesEntity.error
                    )
                    _pokemonSpecies.postValue(state)
                }

                NetworkResponse.Loading -> Unit

                is NetworkResponse.Success -> {
                    val speciesState = PokemonSpeciesUiState(
                        description = pokemonSpeciesEntity.data.description
                    )

                    _pokemonSpecies.postValue(speciesState)
                }
            }
        }
    }

    fun getNextItem(id:Int){
        getPokemon(id.plus(1).toString())
    }

    fun getPreviousItem(id:Int){
        getPokemon(id.minus(1).toString())
    }

    private fun Int.getPokemonId(): String {
        return "#${this.toString().padStart(3, '0')}"
    }

    companion object{
        const val firstItem = 1
        const val lastItem = 150
    }

}