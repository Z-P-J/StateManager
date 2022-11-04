package com.zpj.statemanager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class BaseStateConfig<T extends BaseStateConfig<T>> {

    protected IViewHolder loadingViewHolder;

    protected IViewHolder emptyViewHolder;

    protected IViewHolder errorViewHolder;

    protected IViewHolder loginViewHolder;

    protected IViewHolder noNetworkViewHolder;

    public T setLoadingViewHolder(IViewHolder loadingViewHolder) {
        this.loadingViewHolder = loadingViewHolder;
        return (T) this;
    }

    public T setEmptyViewHolder(IViewHolder emptyViewHolder) {
        this.emptyViewHolder = emptyViewHolder;
        return (T) this;
    }

    public T setErrorViewHolder(IViewHolder errorViewHolder) {
        this.errorViewHolder = errorViewHolder;
        return (T) this;
    }

    public T setLoginViewHolder(IViewHolder loginViewHolder) {
        this.loginViewHolder = loginViewHolder;
        return (T) this;
    }

    public T setNoNetworkViewHolder(IViewHolder noNetworkViewHolder) {
        this.noNetworkViewHolder = noNetworkViewHolder;
        return (T) this;
    }

    public T setErrorView(final int layoutId) {
        return setErrorViewHolder(new EmptyViewHolder(layoutId));
    }

    public T setEmptyView(final int layoutId) {
        return setEmptyViewHolder(new EmptyViewHolder(layoutId));
    }

    public T setLoadingView(int layoutId) {
        return setLoadingViewHolder(new EmptyViewHolder(layoutId));
    }

    public T setLoginView(int layoutId) {
        return setLoginViewHolder(new EmptyViewHolder(layoutId));
    }

    public T setNoNetworkView(int layoutId) {
        return setNoNetworkViewHolder(new EmptyViewHolder(layoutId));
    }

    public T setLoadingView(View view) {
        return setLoadingViewHolder(new ConstantViewHolder(view));
    }

    public T setEmptyView(View view) {
        return setEmptyViewHolder(new ConstantViewHolder(view));
    }

    public T setErrorView(View view) {
        return setErrorViewHolder(new ConstantViewHolder(view));
    }

    public T setLoginView(View view) {
        return setLoginViewHolder(new ConstantViewHolder(view));
    }

    public T setNoNetworkView(View view) {
        return setNoNetworkViewHolder(new ConstantViewHolder(view));
    }

    private int getImageSize(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return Math.min(width, height) / 4;
    }

    public IViewHolder getLoadingViewHolder() {
        if (loadingViewHolder == null) {
            loadingViewHolder = new CustomizedViewHolder() {
                @Override
                public void onViewCreated(View view) {
                    addView(new ProgressBar(context));
                    addTextViewWithPadding(R.string._text_loading, Color.GRAY);
                }
            };
        }
        return loadingViewHolder;
    }

    public IViewHolder getEmptyViewHolder() {
        if (emptyViewHolder == null) {
            emptyViewHolder = new CustomizedViewHolder() {
                @Override
                public void onViewCreated(View view) {
                    int size = getImageSize(view.getContext());
                    addImageView(R.drawable.ic_state_empty, size, size);
                    addTextViewWithPadding(R.string._text_empty, Color.GRAY);
                }
            };
        }
        return emptyViewHolder;
    }

    public IViewHolder getErrorViewHolder() {
        if (errorViewHolder == null) {
            errorViewHolder = new CustomizedViewHolder() {
                @Override
                public void onViewCreated(View view) {
                    this.container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRetry();
                        }
                    });
                    int size = getImageSize(view.getContext());
                    addImageView(R.drawable.ic_state_error, size, size);
                    if (!TextUtils.isEmpty(getMsg())) {
                        int padding = context.getResources().getDimensionPixelSize(R.dimen.text_padding);
                        addTextView("错误信息：" + getMsg(), Color.RED, 14, 2 * padding);
                        addTextView(R.string._text_error, Color.GRAY);
                    } else {
                        addTextViewWithPadding(R.string._text_error, Color.GRAY);
                    }
                }
            };
        }
        return errorViewHolder;
    }

    public IViewHolder getLoginViewHolder() {
        if (loginViewHolder == null) {
            loginViewHolder = new CustomizedViewHolder() {
                @Override
                public void onViewCreated(View view) {
                    int size = getImageSize(view.getContext());
                    addImageView(R.drawable.ic_state_login, size, size);
                    addTextViewWithPadding(R.string._text_login, Color.GRAY);
                    Button button = new Button(context);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onLogin();
                        }
                    });
                    button.setText(R.string._text_btn_login);
                    addView(button);
                }
            };
        }
        return loginViewHolder;
    }

    public IViewHolder getNoNetworkViewHolder() {
        if (noNetworkViewHolder == null) {
            noNetworkViewHolder = new CustomizedViewHolder() {
                @Override
                public void onViewCreated(View view) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRetry();
                        }
                    });
                    int size = getImageSize(view.getContext());
                    addImageView(R.drawable.ic_state_no_network, size, size);
                    addTextViewWithPadding(R.string._text_no_network, Color.GRAY);
                }
            };
        }
        return noNetworkViewHolder;
    }

    public View onCreateView(Context context, State state) {
        IViewHolder viewHolder = getViewHolder(state);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.onCreateView(context);
    }

    public IViewHolder getViewHolder(State state) {
        switch (state) {
            case STATE_LOADING:
                return getLoadingViewHolder();
            case STATE_EMPTY:
                return getEmptyViewHolder();
            case STATE_ERROR:
                return getErrorViewHolder();
            case STATE_LOGIN:
                return getLoginViewHolder();
            case STATE_NO_NETWORK:
                return getNoNetworkViewHolder();
            case STATE_CONTENT:
            default:
                return null;
        }
    }





    private static class EmptyViewHolder extends BaseViewHolder {

        private EmptyViewHolder(int layoutId) {
            super(layoutId);
        }

        @Override
        public void onViewCreated(View view) {

        }
    }

    private static class ConstantViewHolder implements IViewHolder {

        private final View view;

        private ConstantViewHolder(View view) {
            this.view = view;
        }

        @Override
        public View getView() {
            return view;
        }

        @Override
        public int getLayoutId() {
            return 0;
        }

        @Override
        public View onCreateView(Context context) {
            return view;
        }

        @Override
        public void onViewCreated(View view) {

        }

        @Override
        public void onDestroyView() {

        }

    }

}
