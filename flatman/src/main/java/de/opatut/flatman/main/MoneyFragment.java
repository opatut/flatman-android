package de.opatut.flatman.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.opatut.flatman.R;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.Transaction;
import de.opatut.flatman.util.DownloadImageTask;
import de.opatut.flatman.util.Formatter;
import de.opatut.flatman.util.SimpleListAdapter;
import de.opatut.flatman.util.StaticListView;

public class MoneyFragment extends Fragment {

    private StaticListView mTransactionsList;

    public MoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.money, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_money, container, false);

        mTransactionsList = (StaticListView) root.findViewById(R.id.transactions);
        mTransactionsList.setAdapter(new SimpleListAdapter<Transaction>(getActivity(), R.layout.listitem_transaction, DataStorage.getInstance().group.transactions) {
            @Override
            public void fillRow(Transaction transaction, View view) {
                ((TextView) view.findViewById(R.id.reason)).setText(transaction.reason);
                ((TextView) view.findViewById(R.id.amount)).setText(Formatter.formatMoney(transaction.amount));
                ((TextView) view.findViewById(R.id.amount)).setTextColor(getResources().getColor(transaction.to_type.equals("cashbook") ? R.color.money_positive : R.color.money_negative));
                ((TextView) view.findViewById(R.id.date)).setText(Formatter.formatDate(transaction.getDate()));

                String from_text = "";
                if(transaction.from_type.equals("user")) {
                    from_text = transaction.getFromUser().displayname;
                } else if(transaction.from_type.equals("extern")) {
                    from_text = transaction.extern_name;
                } else {
                    from_text = "Cashbook";
                }

                String to_text = "";
                if(transaction.to_type.equals("user")) {
                    to_text = transaction.getToUser().displayname;
                } else if(transaction.to_type.equals("extern")) {
                    to_text = transaction.extern_name;
                } else {
                    to_text = "Cashbook";
                }

                ((TextView) view.findViewById(R.id.from_text)).setText(from_text);
                ((TextView) view.findViewById(R.id.to_text)).setText(to_text);

                view.findViewById(R.id.comment_box).setVisibility(transaction.comment.equals("") ? View.GONE : View.VISIBLE);
                ((TextView) view.findViewById(R.id.comment)).setText(transaction.comment);

                view.findViewById(R.id.extra).setVisibility(View.GONE);


                if(transaction.from_type.equals("user")) {
                    new DownloadImageTask((ImageView) view.findViewById(R.id.from_image)).execute(transaction.getFromUser().getAvatarUrl(64));
                }

                if(transaction.to_type.equals("user")) {
                    new DownloadImageTask((ImageView) view.findViewById(R.id.to_image)).execute(transaction.getToUser().getAvatarUrl(64));
                }

                new DownloadImageTask((ImageView) view.findViewById(R.id.author_image)).execute(transaction.getAuthor().getAvatarUrl(64));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View extra = view.findViewById(R.id.extra);
                        extra.setVisibility(extra.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                });
            }
        });

        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Toast.makeText(getActivity(), "Add dialog...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_filter:
                Toast.makeText(getActivity(), "Filter dialog...", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
