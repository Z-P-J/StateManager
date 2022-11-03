package com.zpj.statemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import static com.zpj.statemanager.State.STATE_CONTENT;
import static com.zpj.statemanager.State.STATE_EMPTY;
import static com.zpj.statemanager.State.STATE_ERROR;
import static com.zpj.statemanager.State.STATE_LOADING;
import static com.zpj.statemanager.State.STATE_LOGIN;
import static com.zpj.statemanager.State.STATE_NO_NETWORK;

public class StateManager extends BaseStateConfig<StateManager> {

    private static StateConfig DEFAULT_CONFIG;

    private final FrameLayout container;

    private final Context context;

    private Runnable onRetry;
    private Runnable onLogin;

    private State state = STATE_CONTENT;

    public interface Action {
        void run(final StateManager manager);
    }

    public View getStateView() {
        return container;
    }

    public static class StateConfig extends BaseStateConfig<StateConfig> { }

    private StateManager(View view) {
        this.context = view.getContext();
        loadingViewHolder = config().loadingViewHolder;
        emptyViewHolder = config().emptyViewHolder;
        errorViewHolder = config().errorViewHolder;
        loginViewHolder = config().loginViewHolder;
        noNetworkViewHolder = config().noNetworkViewHolder;

        container = new FrameLayout(context);

        container.setLayoutParams(view.getLayoutParams());
        if (view.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
            parent.addView(container);
            view.setVisibility(View.GONE);
            container.addView(view);
        }
    }

    public static StateConfig config() {
        if (DEFAULT_CONFIG == null) {
            synchronized (StateManager.class) {
                if (DEFAULT_CONFIG == null) {
                    DEFAULT_CONFIG = new StateConfig();
                }
            }
        }
        return DEFAULT_CONFIG;
    }

    public static StateManager with(View view) {
        return new StateManager(view);
    }

    public StateManager onRetry(final Action action) {
        this.onRetry = new Runnable() {
            @Override
            public void run() {
                if (action != null) {
                    action.run(StateManager.this);
                }
            }
        };
        return this;
    }

    public StateManager onLogin(final Action action) {
        this.onLogin = new Runnable() {
            @Override
            public void run() {
                if (action != null) {
                    action.run(StateManager.this);
                }
            }
        };
        return this;
    }

    public StateManager showContent() {
        changeState(STATE_CONTENT, null);
        return this;
    }

    public StateManager showLoading() {
        changeState(STATE_LOADING, null);
        return this;
    }

    public StateManager showEmpty() {
        changeState(STATE_EMPTY, null);
        return this;
    }

    public StateManager showError(String msg) {
        changeState(STATE_ERROR, msg);
        return this;
    }

    public StateManager showError() {
        changeState(STATE_ERROR, null);
        return this;
    }

    public StateManager showLogin() {
        changeState(STATE_LOGIN, null);
        return this;
    }

    public StateManager showNoNetwork() {
        changeState(STATE_NO_NETWORK, null);
        return this;
    }

    public State getState() {
        return state;
    }

    private void changeState(State state, String msg) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = null;
        View contentView = container.getChildAt(0);
        for (int i = 1; i < container.getChildCount(); i++) {
            container.removeViewAt(i);
        }
        if (state == STATE_CONTENT) {
            contentView.setVisibility(View.VISIBLE);
        } else {
            contentView.setVisibility(View.INVISIBLE);
        }
        switch (state) {
            case STATE_CONTENT:
                ViewGroup.LayoutParams p = contentView.getLayoutParams();
                p.width = ViewGroup.LayoutParams.MATCH_PARENT;
                p.height = ViewGroup.LayoutParams.MATCH_PARENT;
                return;
            case STATE_LOADING:
                view = getLoadingViewHolder().onCreateView(context);
                break;
            case STATE_EMPTY:
                view = getEmptyViewHolder().onCreateView(context);
                break;
            case STATE_ERROR:
                view = getErrorViewHolder().onCreateView(context);
                if (errorViewHolder instanceof BaseViewHolder) {
                    ((BaseViewHolder) errorViewHolder).setOnRetry(onRetry);
                    ((BaseViewHolder) errorViewHolder).setMsg(msg);
                }
                break;
            case STATE_LOGIN:
                view = getLoginViewHolder().onCreateView(context);
                if (loginViewHolder instanceof BaseViewHolder) {
                    ((BaseViewHolder) loginViewHolder).setOnLogin(onLogin);
                }
                break;
            case STATE_NO_NETWORK:
                view = getNoNetworkViewHolder().onCreateView(context);
                if (noNetworkViewHolder instanceof BaseViewHolder) {
                    ((BaseViewHolder) noNetworkViewHolder).setOnRetry(onRetry);
                }
                break;
        }
        if (view != null) {
            container.addView(view, params);
        }

    }

}
