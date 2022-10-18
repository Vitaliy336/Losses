package com.example.losses.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.losses.R
import com.example.losses.repository.LossesRepository
import com.example.losses.repository.model.Statistics
import com.example.losses.view.recyclerView.StatisticData
import java.util.HashMap
import javax.inject.Inject

class ActivityViewModel @Inject constructor(
    val statisticsLivedata: MutableLiveData<Statistics>,
    val errorLivedata: MutableLiveData<Int>,
    val repository: LossesRepository
) : ViewModel() {

    fun fetchStatistics(date: String) {
        repository.getStatistics(statisticsLivedata, errorLivedata, date)
    }

    fun generateStatItems(statItem: Statistics): MutableList<StatisticData> {
        val statItems = mutableListOf<StatisticData>()
        val generalStats = statItem.statisticsMap
        val increase: HashMap<String, Int>? = statItem.increase
        generalStats.forEach { entry ->
            val key = entry.key
            val losses = entry.value
            val increasedBy: Int =
                if(increase != null && increase.isNotEmpty() && increase.containsKey(key))
                    increase[key]!! else -1
            val resources = pickStatResource(key)
            statItems.add(StatisticData(key,
                losses,
                increasedBy,
                resources.first,
                resources.second))

        }
        return statItems
    }

    private fun pickStatResource(key: String): Pair<Int, Int> {
        return when (key) {
            "personnel_units" -> Pair(R.string.personnel_units, R.drawable.ic_icon_units)
            "tanks" -> Pair(R.string.tanks, R.drawable.ic_icon_tanks)
            "armoured_fighting_vehicles" -> Pair(R.string.armoured_fighting_vehicles,
                R.drawable.ic_icon_armored_vehicles)
            "artillery_systems" -> Pair(R.string.artillery_systems,
                R.drawable.ic_icon_artillery)
            "mlrs" -> Pair(R.string.mlrs, R.drawable.ic_icon_mlrs)
            "aa_warfare_systems" -> Pair(R.string.aa_warfare_systems, R.drawable.ic_icon_ppo)
            "planes" -> Pair(R.string.planes, R.drawable.ic_icon_planes)
            "helicopters" -> Pair(R.string.helicopters, R.drawable.ic_icon_helicopters)
            "vehicles_fuel_tanks" -> Pair(R.string.vehicles_fuel_tanks,
                R.drawable.ic_icon_fuel_auto)
            "warships_cutters" -> Pair(R.string.warships_cutters, R.drawable.ic_icon_ships)
            "cruise_missiles" -> Pair(R.string.cruise_missiles, R.drawable.ic_icon_rocket)
            "uav_systems" -> Pair(R.string.uav_systems, R.drawable.ic_icon_bpla)
            "special_military_equip" -> Pair(R.string.special_military_equip,
                R.drawable.ic_icon_special)
            "atgm_srbm_systems" -> Pair(R.string.atgm_srbm_systems, R.drawable.ic_icon_trk)
            else -> Pair(-1, -1)
        }
    }
}