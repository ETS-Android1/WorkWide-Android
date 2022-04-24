package com.example.workwide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class onBoardingFragment1 extends Fragment {
    TextView skip1;
    SharedPreferences sesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding1, container, false);
        skip1 = root.findViewById(R.id.skip1);
        skip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesion = getContext().getSharedPreferences("SESION", Context.MODE_PRIVATE);
                int id = sesion.getInt("id", 0);
                int tipo = sesion.getInt("tipo", 0);
                String region = sesion.getString("region", "");
                if(id == 0){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    if(tipo == 2){
                        if(region.equals("")){
                            Intent intent = new Intent(getActivity(), activity_compReg.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getActivity(), activity_profileIndex.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Intent intent = new Intent(getActivity(), activity_profileIndex.class);
                        startActivity(intent);
                    }
                }
            }
        });

        return root;
    }
}
