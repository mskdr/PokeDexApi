package com.muhammetkdr.pokemondex.common

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.muhammetkdr.pokemondex.R
import com.muhammetkdr.pokemondex.data.dto.pokemon.Pokemon
import com.muhammetkdr.pokemondex.data.dto.pokemonlist.PokemonList
import com.muhammetkdr.pokemondex.data.dto.pokemonspecies.PokemonSpecies
import com.muhammetkdr.pokemondex.domain.model.PokemonEntity
import com.muhammetkdr.pokemondex.domain.model.PokemonListEntity
import com.muhammetkdr.pokemondex.domain.model.PokemonSpeciesEntity
import retrofit2.Response
import java.util.Locale

fun ImageView.setPokemonImage(pokePath: String?) {
    Glide.with(this).load(pokePath)
        .placeholder(R.drawable.ic_downloading)
        .error(R.drawable.ic_error)
        .into(this)
}

fun getPokemonColorByType(pokemonType: String): Int = when (pokemonType.lowercase()) {
    "bug" -> R.color.pokemon_type_bug

    "dark" -> R.color.pokemon_type_dark

    "dragon" -> R.color.pokemon_type_dragon

    "electric" -> R.color.pokemon_type_electric

    "fairy" -> R.color.pokemon_type_fairy

    "fighting" -> R.color.pokemon_type_fighting

    "fire" -> R.color.pokemon_type_fire

    "flying" -> R.color.pokemon_type_flying

    "ghost" -> R.color.pokemon_type_ghost

    "grass" -> R.color.pokemon_type_grass

    "ground" -> R.color.pokemon_type_ground

    "ice" -> R.color.pokemon_type_ice

    "normal" -> R.color.pokemon_type_normal

    "poison" -> R.color.pokemon_type_poison

    "psychic" -> R.color.pokemon_type_psychic

    "rock" -> R.color.pokemon_type_rock

    "steel" -> R.color.pokemon_type_steel

    "water" -> R.color.pokemon_type_water

    else -> R.color.pokemon_type_normal
}

inline fun <I, O> I.mapTo(crossinline mapper: (I) -> O): O {
    return mapper(this)
}

fun Response<PokemonList>.toPokemonListEntity(): List<PokemonListEntity> {
    return body()!!.results!!.map {
        PokemonListEntity(
            it.name.orEmpty()
        )
    }
}

fun Response<Pokemon>.toPokemonEntity(): PokemonEntity {
    return body()!!.run {
        PokemonEntity(
            weight = ((weight!!*0.1f).toString().take(5) + " kg").replace('.',','),
            height = ((height!!*0.1f).toString().take(5) + " m").replace('.',','),
            moves = abilities!!.map { it.ability!!.name.orEmpty().capitalizeMovesData() },
            pokeTypes = types!!.map {
                it.type!!.name!!.capitalizeWords()
            },
            hpStat =  stats!!.first { it.stat!!.name == "hp" }.baseStat.toString(),
            hpStatProgress = stats.first { it.stat!!.name == "hp" }.baseStat ?: 0,
            attackStat = stats.first { it.stat!!.name == "attack" }.baseStat.toString(),
            attackStatProgress = stats.first { it.stat!!.name == "attack" }.baseStat ?: 0,
            defenseStat = stats.first { it.stat!!.name == "defense" }.baseStat.toString(),
            defenseStatProgress = stats.first { it.stat!!.name == "defense" }.baseStat ?: 0,
            specialAttackStat = stats.first { it.stat!!.name == "special-attack" }.baseStat.toString(),
            specialAttackStatProgress = stats.first { it.stat!!.name == "special-attack" }.baseStat ?: 0,
            specialDefenseStat = stats.first { it.stat!!.name == "special-defense" }.baseStat.toString(),
            specialDefenseStatProgress = stats.first { it.stat!!.name == "special-defense" }.baseStat ?: 0,
            speedStat = stats.first { it.stat!!.name == "speed" }.baseStat.toString(),
            speedStatProgress = stats.first { it.stat!!.name == "speed" }.baseStat ?: 0,
            pokemonColor = getPokemonColorByType( types.first().type!!.name!!)
        )
    }
}

fun Response<PokemonSpecies>.toPokemonSpeciesEntity(): PokemonSpeciesEntity{
    return body()!!.run {
        PokemonSpeciesEntity(
            description = flavorTextEntries!![9].flavorText.orEmpty().replace("\n","")
        )
    }
}

inline fun <T> LiveData<T>.observeIfNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner) { data ->
        if (data != null) {
            observer(data)
        }
    }
}

fun TextView.setColor(@ColorRes colorId: Int){
    this.setTextColor(ContextCompat.getColor(this.context,colorId))
}

fun ProgressBar.setProgressColor(@ColorRes colorId: Int){
    this.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context,colorId))
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
    word.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
}

fun String.capitalizeMovesData(): String = split("-").joinToString("-") { word ->
    word.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.showSnackbar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
}
