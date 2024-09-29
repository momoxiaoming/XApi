package com.xm.xapi.base.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder

/**
 * ApiBindingViewBinder
 *
 * @author mmxm
 * @date 2024/9/9 11:01
 */
abstract class ApiBindingViewBinder<T, DB : ViewDataBinding>(val itemViewId: Int) :
    ItemViewBinder<T, ApiBindingViewBinder.ApiBindingViewHolder<DB>>() {

    abstract fun bindData(data: T, binding: DB)

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ApiBindingViewBinder.ApiBindingViewHolder<DB> {
        return ApiBindingViewHolder<DB>(inflater.inflate(itemViewId, parent, false))
    }

    override fun onBindViewHolder(holder: ApiBindingViewBinder.ApiBindingViewHolder<DB>, item: T) {
        bindData(item, holder.binding)
        holder.binding.executePendingBindings()
    }

    class ApiBindingViewHolder<DB : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<DB>(itemView)!!
    }
}