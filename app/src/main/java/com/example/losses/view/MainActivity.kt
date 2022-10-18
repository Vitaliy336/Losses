package com.example.losses.view

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.losses.LossesApplication
import com.example.losses.R
import com.example.losses.databinding.ActivityMainBinding
import com.example.losses.repository.model.Statistics
import com.example.losses.view.recyclerView.StatsAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), View.OnClickListener,
    MaterialPickerOnPositiveButtonClickListener<Long> {

    @Inject
    lateinit var activityViewModel: ActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: StatsAdapter

    companion object {
        private const val dateFormat = "yyyy-MM-dd"
        private const val zoneId = "GTM+3"
        private const val materialDataPickerTag = "MATERIAL_DATE_PICKER"
        private const val itemsSpanCount = 2
    }

    private val statisticsObserver: Observer<Statistics> by lazy {
        Observer<Statistics> {
            changeRecyclerViewVisibility(VISIBLE)
            changeProgressViewVisibility(GONE)
            val items = activityViewModel.generateStatItems(it)
            binding.dayCounter.text = "${resources.getString(R.string.day_number)}${it.day}"
            recyclerViewAdapter.setStatistics(items)
        }
    }

    private val errorMessageObserver: Observer<Int> by lazy {
        Observer<Int> {
            changeRecyclerViewVisibility(GONE)
            changeProgressViewVisibility(GONE)
            Snackbar.make(binding.cardView, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private val calendar by lazy {
        Calendar.getInstance()
    }

    private val simpleDateFormat by lazy {
        SimpleDateFormat(dateFormat, resources.configuration.locales[0])
            .apply {
                timeZone = TimeZone.getTimeZone(zoneId)
            }
    }

    private val datePickerBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        (application as LossesApplication).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener(this)
        binding.prevBtn.setOnClickListener(this)
        datePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        initAdapter()
        initDatePicker()
        subscribeForUpdates()
        fetchContent(getDateString())
        activityViewModel.fetchStatistics(getDateString())
    }

    override fun onPositiveButtonClick(selection: Long) {
        val date = simpleDateFormat.format(Date(selection))
        changeRecyclerViewVisibility(GONE)
        changeProgressViewVisibility(VISIBLE)
        binding.datePicker.text = date
        activityViewModel.fetchStatistics(getDateString())
    }

    override fun onClick(view: View?) {
        if(view != null) {
            when (view.id) {
                R.id.next_btn -> {
                    changeRecyclerViewVisibility(GONE)
                    changeProgressViewVisibility(VISIBLE)
                    binding.datePicker.text =
                        getUpdatedDateString(getDateString(), 1)
                    datePickerBuilder.setSelection(calendar.timeInMillis)
                    activityViewModel.fetchStatistics(getDateString())
                }
                R.id.prev_btn -> {
                    changeRecyclerViewVisibility(GONE)
                    changeProgressViewVisibility(VISIBLE)
                    binding.datePicker.text =
                        getUpdatedDateString(getDateString(), -1)
                    datePickerBuilder.setSelection(calendar.timeInMillis)
                    activityViewModel.fetchStatistics(getDateString())
                }
                R.id.date_picker -> {
                    calendar.time = simpleDateFormat.parse(getDateString())
                    datePickerBuilder.apply {
                        setSelection(calendar.timeInMillis)
                        setCalendarConstraints(CalendarConstraints.Builder()
                            .setOpenAt(calendar.timeInMillis)
                            .build())
                    }
                    val datePicker = datePickerBuilder.build()
                    datePicker.show(supportFragmentManager, materialDataPickerTag)
                    datePicker.addOnPositiveButtonClickListener(this)
                }
            }
        }
    }

    private fun subscribeForUpdates() {
        activityViewModel.statisticsLivedata.observe(this, statisticsObserver)
        activityViewModel.errorLivedata.observe(this, errorMessageObserver)
    }

    private fun fetchContent(date: String) {
        activityViewModel.fetchStatistics(date)
    }

    private fun initDatePicker() {
        val currentDate = simpleDateFormat.format(calendar.time)
        binding.datePicker.text = currentDate
        binding.datePicker.setOnClickListener(this)
    }

    private fun initAdapter() {
        changeProgressViewVisibility(VISIBLE)
        binding.contentRv.apply {
            recyclerViewAdapter = StatsAdapter()
            adapter = recyclerViewAdapter
            layoutManager = GridLayoutManager(this@MainActivity, itemsSpanCount)
        }
    }

    private fun getUpdatedDateString(currentDate: String, addDays: Int): String {
        val date: Date? = simpleDateFormat.parse(currentDate)
        return if(date != null) {
            calendar.time = date
            calendar.add(Calendar.DATE, addDays)
            calendar.time
            simpleDateFormat.format(calendar.time)
        } else {
            ""
        }
    }

    private fun getDateString() = binding.datePicker.text.toString()

    private fun changeRecyclerViewVisibility(visibility: Int) {
        binding.contentRv.visibility = visibility
    }

    private fun changeProgressViewVisibility(visibility: Int) {
        binding.progressBar.visibility = visibility
    }
}