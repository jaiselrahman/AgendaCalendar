package com.jaiselrahman.agendacalendar.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaiselrahman.agendacalendar.model.BaseEvent;

import java.time.LocalDate;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;

public class AgendaView extends RecyclerView {
    @SuppressWarnings("rawtypes")
    private EventAdapter eventAdapter = null;
    private LinearLayoutManager linearLayoutManager;

    public AgendaView(@NonNull Context context) {
        this(context, null);
    }

    public AgendaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgendaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) return;

        linearLayoutManager = new LinearLayoutManager(context);
        super.setLayoutManager(linearLayoutManager);

        setItemAnimator(null);
        setNestedScrollingEnabled(true);
    }

    public void scrollTo(LocalDate localDate) {
        if (eventAdapter == null) return;

        int pos = eventAdapter.getAdapterPosition(localDate);

        if (pos >= eventAdapter.getItemCount())
            pos = eventAdapter.getItemCount() - 1;

        if (pos >= 0) {
            linearLayoutManager.scrollToPositionWithOffset(pos, 0);
        }
    }

    @Override
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public final void setLayoutManager(@Nullable LayoutManager layout) {
        if (isInEditMode()) {
            super.setLayoutManager(layout);
            return;
        }
        throw new RuntimeException("LayoutManager cannot be changed");
    }

    @Override
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public final void setAdapter(@Nullable Adapter adapter) {
        if (adapter instanceof EventAdapter) {
            //noinspection rawtypes
            eventAdapter = (EventAdapter) adapter;
            super.setAdapter(eventAdapter);
            addItemDecoration(new StickyHeaderDecoration(eventAdapter.getDayHeader(), false));
            return;
        }
        if (isInEditMode()) {
            super.setAdapter(adapter);
            return;
        }
        throw new RuntimeException("Adapter should not be changed");
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    final BaseEvent firstVisibleEvent() {

        int pos = linearLayoutManager.findFirstVisibleItemPosition();
        if (pos == NO_POSITION) return null;
        return eventAdapter.getEvent(pos);
    }
}
