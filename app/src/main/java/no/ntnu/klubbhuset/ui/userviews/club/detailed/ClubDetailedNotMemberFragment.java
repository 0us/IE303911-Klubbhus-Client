package no.ntnu.klubbhuset.ui.userviews.club.detailed;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.klubbhuset.R;
import no.ntnu.klubbhuset.data.model.Club;

/**
 * Displays information relating to a user who is NOT member of an organization,
 * like a join button
 */
public class ClubDetailedNotMemberFragment extends Fragment {

    private ClubDetailedViewModel mViewModel;
    private ClubDetailedFragment.onMembershipStatusChangedListener mListener;

    public static ClubDetailedNotMemberFragment newInstance() {
        return new ClubDetailedNotMemberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_club_detailed_not_member, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClubDetailedFragment.onMembershipStatusChangedListener) {
            mListener = (ClubDetailedFragment.onMembershipStatusChangedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onMembershipStatusChangedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        mViewModel = ViewModelProviders.of(this).get(ClubDetailedViewModel.class);
        Club club = ClubDetailedViewModel.getCurrentClub();
        Button joinClubBtn = getView().findViewById(R.id.club_detailed_joinbtn);
        joinClubBtn.setOnClickListener(click -> {
            mViewModel.joinClub(club.getOid()).observe(this, response -> {
                mListener.onMembershipStatusChanged(response);
            });
        });
    }

}
