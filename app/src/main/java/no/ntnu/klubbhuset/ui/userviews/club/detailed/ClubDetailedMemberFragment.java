package no.ntnu.klubbhuset.ui.userviews.club.detailed;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import no.ntnu.klubbhuset.R;
import no.ntnu.klubbhuset.data.model.Club;
import no.ntnu.klubbhuset.data.model.Member;
import no.ntnu.klubbhuset.viewmodels.ClubDetailedViewModel;


/**
 * Displays information relating to a users membership in an organization, like
 * payment-information etc
 */
public class ClubDetailedMemberFragment extends Fragment {

    private ClubDetailedViewModel mViewModel;
    private Member member;

    public static ClubDetailedMemberFragment newInstance(Member member) {
        Bundle args = new Bundle();
        args.putSerializable("member", member);
        ClubDetailedMemberFragment newInstance = new ClubDetailedMemberFragment();
        newInstance.setArguments(args);
        return newInstance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_club_detailed_member, container, false);
        this.member = (Member) getArguments().getSerializable("member");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ClubDetailedViewModel.class);

        TextView memberSince = getView().findViewById(R.id.club_detailed_member_since);
        SimpleDateFormat format  =new SimpleDateFormat("dd MMMM yyyy");
        memberSince.setText(format.format(member.getCreated()));
        /*mViewModel.getMembership(club).observe(this, l-> {

        });*/
    }

}
