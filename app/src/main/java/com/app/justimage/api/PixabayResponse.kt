package com.app.justimage.api

import com.app.justimage.data.PixabayPhoto

data class PixabayResponse(var total : Int, var totalHits : Int, var hits : List<PixabayPhoto>)