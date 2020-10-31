package com.dacodes.OscarOlivares

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray

data class Movies(
    var id:String,
    var titulo:String,
    var poster:String,
    var fecha:String,
    var rate:Double,
    var descripcion:String,
    var duracion:String,
    var generos:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titulo)
        parcel.writeString(poster)
        parcel.writeString(fecha)
        parcel.writeDouble(rate)
        parcel.writeString(descripcion)
        parcel.writeString(duracion)
        parcel.writeString(generos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movies> {
        override fun createFromParcel(parcel: Parcel): Movies {
            return Movies(parcel)
        }

        override fun newArray(size: Int): Array<Movies?> {
            return arrayOfNulls(size)
        }
    }
}
