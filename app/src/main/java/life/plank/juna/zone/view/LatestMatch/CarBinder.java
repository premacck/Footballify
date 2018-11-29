package life.plank.juna.zone.view.LatestMatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahamed.multiviewadapter.BaseViewHolder;
import com.ahamed.multiviewadapter.ItemBinder;

import life.plank.juna.zone.R;

class CarBinder extends ItemBinder<CarModel, CarBinder.CarViewHolder> {

    @Override
    public CarViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new CarViewHolder(inflater.inflate(R.layout.item_car, parent, false));
    }

    @Override
    public void bind(CarViewHolder holder, CarModel item) {
        // Bind the data here
        holder.tx1.setText(item.name);
        holder.tx2.setText(item.description);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof CarModel;
    }

    class CarViewHolder extends BaseViewHolder<CarModel> {
        TextView tx1;
        TextView tx2;

        CarViewHolder(View itemView) {
            super(itemView);
            tx1 = itemView.findViewById(R.id.car_name);
            tx2 = itemView.findViewById(R.id.car_description);
        }
        // Normal ViewHolder code
    }
}
