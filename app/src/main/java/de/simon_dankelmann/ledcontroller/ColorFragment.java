package de.simon_dankelmann.ledcontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;

public class ColorFragment extends Fragment {

    private LedController ledController;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ledController = new LedController(this.getActivity());

        // CONNECT COLORPICKER AND OPACITYBAR
        ColorPicker picker = (ColorPicker)this.getActivity().findViewById(R.id.pickerTab);
        OpacityBar opacityBar = (OpacityBar)this.getActivity().findViewById(R.id.opacitybarTab);
        picker.addOpacityBar(opacityBar);

        //SET COLORPICKER LISTENER
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                ledController.changeColor(color);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color, container, false);
    }

}
