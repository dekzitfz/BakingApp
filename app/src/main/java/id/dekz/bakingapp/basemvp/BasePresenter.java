package id.dekz.bakingapp.basemvp;

/**
 * Created by DEKZ on 6/27/2017.
 */

public interface BasePresenter<T extends BaseView> {
    void onAttach(T BaseView);
    void onDetach();
}
