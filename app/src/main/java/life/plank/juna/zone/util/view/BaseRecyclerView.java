package life.plank.juna.zone.util.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * This is the base RecyclerView class for ease in creating Adapters with DiffUtil and Dagger injection.<br/><br/>
 */
public class BaseRecyclerView {

    public abstract static class Adapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

        public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.bind();
        }

        /**
         * This method first checks for the payloads and updates them in the list, if any.<br/>
         * Else it binds from the given list.
         */
        @Override
        public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
            if (payloads != null) {
                if (!payloads.isEmpty()) {
                    holder.bind(payloads);
                } else holder.bind();
            } else holder.bind();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public abstract int getItemCount();

        /**
         * Implement this method to specify how to dispose the adapter when it is no longer in use.
         */
        public void release() {
        }

        /**
         * Implement this method to <b>make sure</b> the data is updated on the UI thread.
         */
        public void notifyDataChanged(Activity activity) {
            activity.runOnUiThread(this::notifyDataSetChanged);
        }
    }

    public abstract static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * Bind the Data to the views here.
         */
        public void bind() {
        }

        /**
         * Use this method when updating the RecyclerView data with DiffUtil.
         *
         * @param payloads the payload Bundle to be passed.
         */
        public void bind(List<Object> payloads) {
        }
    }
}