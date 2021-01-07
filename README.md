# StateManager
 A library that can easily switch different states like empty、error、loading、content and so on.

## Usage

step 1:

```java
implementation 'com.zpj.widget:StateManager:1.0.0'
```

step 2:

```java
StateManager manager = StateManager.with(view)
//                .setRecyclable(true)
                .setNoNetworkView(R.layout.item_text_grid)
                .onRetry(new StateManager.Action() {
                    @Override
                    public void run(final StateManager manager) {
                        manager.showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        manager.showContent();
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .onLogin(new StateManager.Action() {
                    @Override
                    public void run(StateManager manager) {
                        Toast.makeText(StateActivity.this, "onLogin", Toast.LENGTH_SHORT).show();
                    }
                })
                .showLoading();
```
