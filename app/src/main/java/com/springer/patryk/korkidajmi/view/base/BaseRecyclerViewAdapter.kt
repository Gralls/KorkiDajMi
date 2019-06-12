package pl.mauto24.app.view.base

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class BaseRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> :
		RecyclerView.Adapter<VH>() {

	protected var mItems: ArrayList<T> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		return setViewHolder(parent)
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		bindView(mItems[position], holder)
	}

	override fun getItemCount(): Int = mItems.size

	protected abstract fun setViewHolder(parent: ViewGroup): VH
	protected abstract fun bindView(item: T, viewHolder: VH)

	open fun setDataset(items: ArrayList<T>) {
		this.mItems = items
		notifyDataSetChanged()
	}
}