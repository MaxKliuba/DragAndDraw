package com.maxclub.android.draganddraw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class DragAndDrawFragment extends Fragment {

    BoxDrawingView mBoxDrawingView;

    public static Fragment newInstance() {
        return new DragAndDrawFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drag_and_draw, container, false);

        mBoxDrawingView = (BoxDrawingView) v.findViewById(R.id.box_drawing_view);
        mBoxDrawingView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                getActivity().invalidateOptionsMenu();
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_drag_and_draw_menu, menu);

        MenuItem undoItem = menu.findItem(R.id.undo_item);
        undoItem.setVisible(mBoxDrawingView.isUndoEnable());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.undo_item:
                mBoxDrawingView.undo();
                return true;
            case R.id.clear_screen_item:
                mBoxDrawingView.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
