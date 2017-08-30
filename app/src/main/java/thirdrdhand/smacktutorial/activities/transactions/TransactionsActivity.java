package thirdrdhand.smacktutorial.activities.transactions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import thirdrdhand.smacktutorial.R;
import thirdrdhand.smacktutorial.Storage.BackEndDB;
import thirdrdhand.smacktutorial.xmpp.XmppService;

public class TransactionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        RecyclerView transactionRecycle = (RecyclerView) findViewById(R.id.transactionList);
        transactionRecycle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        transactionRecycle.setLayoutManager(llm);
        List<Transaction> transList = BackEndDB.getInstance(XmppService.getContext()).getTransactionList();
        transactionRecycle.setAdapter(new TransactionAdapter(transList));

    }

}
