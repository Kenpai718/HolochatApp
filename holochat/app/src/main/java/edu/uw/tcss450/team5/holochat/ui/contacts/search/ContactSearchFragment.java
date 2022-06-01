package edu.uw.tcss450.team5.holochat.ui.contacts.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.uw.tcss450.team5.holochat.databinding.FragmentContactSearchBinding;
import edu.uw.tcss450.team5.holochat.model.UserInfoViewModel;
import edu.uw.tcss450.team5.holochat.ui.chats.chatroom.ChatRoomFragmentDirections;
import edu.uw.tcss450.team5.holochat.ui.contacts.contact_tabs.ContactTabFragmentDirections;
import edu.uw.tcss450.team5.holochat.ui.contacts.info.Contact;
import edu.uw.tcss450.team5.holochat.ui.contacts.info.ContactViewModel;

/**
 * Fragment that hold tools to search for a user of the app and send a friend request
 * @author Tarnveer
 */
public class ContactSearchFragment extends Fragment {
    private UserInfoViewModel mUserModel;
    private ContactViewModel mContactViewModel;
    private ArrayList<String> mContacts;
    private FragmentContactSearchBinding binding;
    private AllMemberListViewModel mModel;
    private SearchViewModel mSearchModel;
    public ContactSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentContactSearchBinding.inflate(inflater);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider provider= new ViewModelProvider(getActivity());
        mContactViewModel = provider.get(ContactViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);

        mModel = provider.get(AllMemberListViewModel.class);
        mModel.connectGet(mUserModel.getJwt(), mUserModel.getMemberID());
        mSearchModel = provider.get(SearchViewModel.class);
        mSearchModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSearchResponse);
        Log.i("CONTACT_SEARCH", "" + mUserModel.getMemberID());

        mContactViewModel.connect(mUserModel.getEmail(), mUserModel.getJwt());
        mContactViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeContacts);
        binding.buttonFindContact.setOnClickListener(this::attemptToFind);

        mModel.addContactRequestListObserver(getViewLifecycleOwner(), contactList -> {
            //if (!contactList.isEmpty()) {
            binding.listRoot.setAdapter(
                    new AllMemberRecyclerViewAdapter(contactList, getActivity().getSupportFragmentManager())
            );
        });

    }

    private void attemptToFind(View view) {
        String input = String.valueOf(binding.connectionsSearchEditText.getText());
        Log.i("CONTACT_SEARCH_FRAG", "input:" + input);

        navigateToUserFound("kenahren@gmail.com","Kenpai",26);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observeSearchResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.connectionsSearchEditText.setError(
                        "Can't find this user.");
            } else {
                //TODO
                navigateToUserFound("kenahren@gmail.com","Kenpai",26);
            }
        }
    }

    /**
     * Successfully searched now navigate to the user fragment
     */
    private void navigateToUserFound(String email, String username, int memberid) {
        Navigation.findNavController(getView())
                .navigate(ContactTabFragmentDirections.
                        actionNavigationTabbedContactsToContactFoundFragment(
                                email,username, memberid));
    }





    private void observeContacts(JSONObject response) {
        Log.i("user", response.toString());
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
//                    binding.contactNames.setText(
//                            mUserModel.getEmail());
                    binding.contactNames.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    getContacts(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void getContacts(JSONObject response) throws JSONException {
        Log.i("contacts", response.toString());
        ArrayList<String> contacts = new ArrayList<String>();

        JSONArray jsonUsers = response.getJSONArray("values");
        for(int i = 0; i < jsonUsers.length(); i++) {
            JSONObject user = (new JSONObject(jsonUsers.getString(i)));
            contacts.add(user.getString("username"));
        }

        StringBuilder builder = new StringBuilder();
        for(int j = 0; j < contacts.size(); j++) {
            mContacts.add(contacts.get(j));
            builder.append(contacts.get(j) + "\n\n");
        }
        Log.i("contacts", builder.toString());
        binding.contactNames.setText(builder.toString());
    }
}