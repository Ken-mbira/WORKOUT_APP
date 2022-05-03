package devmbira.mobile.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import devmbira.mobile.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseAdapterStatus(val items: ArrayList<ExerciseModel>)
    :RecyclerView.Adapter<ExerciseAdapterStatus.ViewHolder>() {

    class ViewHolder(binding : ItemExerciseStatusBinding):
                RecyclerView.ViewHolder(binding.root){
                val tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.tvItem.text = model.getId().toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}