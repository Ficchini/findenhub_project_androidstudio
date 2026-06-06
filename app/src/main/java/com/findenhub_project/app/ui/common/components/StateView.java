// ui/common/components/StateView.java
// View customizada que gerencia 3 estados mutuamente exclusivos:
// LOADING, EMPTY e ERROR. Evita código duplicado nos Fragments.
package com.findenhub_project.app.ui.common.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import com.findenhub_project.app.R;

public class StateView extends ConstraintLayout {

    public enum State { LOADING, EMPTY, ERROR, HIDDEN }

    private CircularProgressIndicator progressBar;
    private TextView tvMessage;
    private Button btnRetry;

    public StateView(Context context) {
        super(context);
        init(context);
    }

    public StateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.activity_state_view, this, true);
        progressBar = findViewById(R.id.state_progress);
        tvMessage   = findViewById(R.id.state_message);
        btnRetry    = findViewById(R.id.state_btn_retry);
        setVisibility(GONE);
    }

    public void setState(State state, @Nullable String message,
                         @Nullable Runnable retryAction) {
        switch (state) {
            case LOADING:
                setVisibility(VISIBLE);
                progressBar.setVisibility(VISIBLE);
                tvMessage.setVisibility(GONE);
                btnRetry.setVisibility(GONE);
                break;

            case EMPTY:
                setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                tvMessage.setVisibility(VISIBLE);
                tvMessage.setText(message != null ? message : "Nenhum item encontrado.");
                btnRetry.setVisibility(GONE);
                break;

            case ERROR:
                setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                tvMessage.setVisibility(VISIBLE);
                tvMessage.setText(message != null ? message : "Erro ao carregar dados.");
                if (retryAction != null) {
                    btnRetry.setVisibility(VISIBLE);
                    btnRetry.setOnClickListener(v -> retryAction.run());
                } else {
                    btnRetry.setVisibility(GONE);
                }
                break;

            case HIDDEN:
                setVisibility(GONE);
                break;
        }
    }

    /** Atalho para estado de loading */
    public void showLoading() {
        setState(State.LOADING, null, null);
    }

    /** Atalho para esconder */
    public void hide() {
        setState(State.HIDDEN, null, null);
    }
}