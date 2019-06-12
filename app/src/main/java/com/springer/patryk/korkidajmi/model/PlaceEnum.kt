package com.springer.patryk.korkidajmi.model

enum class PlaceEnum {
	TUTOR {
		override fun getPlaceName(): String = "U korepetytora"
	},
	STUDENT {
		override fun getPlaceName(): String = "U ucznia"
	};

	abstract fun getPlaceName(): String
}