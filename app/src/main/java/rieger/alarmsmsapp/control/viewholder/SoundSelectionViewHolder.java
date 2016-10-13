package rieger.alarmsmsapp.control.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;

/**
 * Created by sebastian on 13.10.16.
 */

public class SoundSelectionViewHolder extends RecyclerView.ViewHolder {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.list_item_sound_name)
    TextView soundName;

    @Bind(R.id.list_item_sound_active)
    ImageView isActive;

    public SoundSelectionViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public TextView getSoundName() {
        return soundName;
    }

    public ImageView getIsActive() {
        return isActive;
    }
}
