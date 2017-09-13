package com.sky.model.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontTextview;

/**
 * Created by Administrator on 17-9-12.
 */
public class LoginManager implements View.OnClickListener{

    private Context context;
    private View view;
    private IconFontTextview icon_self;

    public LoginManager(Context context){
        this.context = context;
    }


    public void setView(View view) {
        this.view = view;
        icon_self = (IconFontTextview) view.findViewById(R.id.icon_self);
    }

    public void init() {
        icon_self.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.icon_self:
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
