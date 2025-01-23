package com.example.gr2sw2024b_mams

import android.os.Parcel
import android.os.Parcelable

class BEntrenador (
    var id: Int,
    var nombre: String,
    var descripcion: String?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        if(parcel.readString() == null) "" else parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "$nombre ${descripcion}"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(nombre)
        dest.writeString(descripcion)
    }

    companion object CREATOR : Parcelable.Creator<BEntrenador> {
        override fun createFromParcel(parcel: Parcel): BEntrenador {
            return BEntrenador(parcel)
        }

        override fun newArray(size: Int): Array<BEntrenador?> {
            return arrayOfNulls(size)
        }
    }
}