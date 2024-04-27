package com.example.furniturebuyandsell.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniturebuyandsell.R;
import com.example.furniturebuyandsell.adapter.FeaturedProductAdapter;
import com.example.furniturebuyandsell.adapter.SearchResultAdapter;
import com.example.furniturebuyandsell.common.Constants;
import com.example.furniturebuyandsell.fragment.products.DetailProductDialogFragment;
import com.example.furniturebuyandsell.interfaces.ApiService;
import com.example.furniturebuyandsell.interfaces.ProductItemClickListener;
import com.example.furniturebuyandsell.model.AuthResponse;
import com.example.furniturebuyandsell.model.Product;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements ProductItemClickListener {
    AutoCompleteTextView autoCompleteTextView;
    ImageButton btn_search;
    RecyclerView recyclerResult;
    SearchResultAdapter searchResultAdapter;
    ArrayList<String> historySearch;
    ArrayAdapter<String> adapterHistorySearch;
    private final SearchFragment self = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        autoCompleteTextView = view.findViewById(R.id.auCTxt_keyword);
        btn_search = view.findViewById(R.id.btn_search);
        recyclerResult = view.findViewById(R.id.recyV_result);
        // Khởi tạo ArrayList nếu chưa tồn tại
        if (historySearch == null) {
            historySearch = new ArrayList<>();
        }
        // Khởi tạo adapter và gán cho AutoCompleteTextView
        adapterHistorySearch = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, historySearch);
        autoCompleteTextView.setAdapter(adapterHistorySearch);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !historySearch.isEmpty()) {
                    autoCompleteTextView.showDropDown();
                    Log.d("SHOW_HISTORY_SEARCH", String.valueOf(autoCompleteTextView));
                }
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = autoCompleteTextView.getText().toString();
                historySearch.add(keyword);
                Log.d("HISTORY_SEARCH", String.valueOf(historySearch));
                //thông báo lịch sử tìm kiếm đã đc thay đổi
                adapterHistorySearch.notifyDataSetChanged();
                if (!keyword.equals("")){
                    performSearch(keyword);
                }else {
                    recyclerResult.setAdapter(null);
                    recyclerResult.setLayoutManager(null);
                }


            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedKeyword = (String) parent.getItemAtPosition(position);
                performSearch(selectedKeyword);
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Thực hiện hành động tìm kiếm ở đây
                    if (!textView.getText().toString().equals("")){
                        performSearch(textView.getText().toString());
                    }else {
                        recyclerResult.setAdapter(null);
                        recyclerResult.setLayoutManager(null);
                    }
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    private void performSearch(String keyword) {
        ApiService apiService = Constants.getApiService();
        Call<ArrayList<Product>> call = apiService.searchInfor(keyword);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    // Xử lý kết quả tìm kiếm ở đây
                    ArrayList<Product> productList = response.body();
                    for (Product product : productList){
                        Log.d("Search result", "ID"+ product.getId() + "   name" + product.getNamePr());
                    }
                    if (!productList.isEmpty()) {
                        // Hiển thị sản phẩm trên RecyclerView
                        searchResultAdapter = new SearchResultAdapter(productList, requireContext());
                        searchResultAdapter.setListener(self);
                        recyclerResult.setAdapter(searchResultAdapter);

                        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerResult.setLayoutManager(linearLayoutManager);

                    } else {
                        // Hiển thị thông báo nếu không có sản phẩm nào
                        Toast.makeText(getContext(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi khi tìm kiếm", Toast.LENGTH_SHORT).show();
                Log.e("SEARCH", "ERR", t);
            }
        });
    }
    @Override
    public void onItemClick(Product product) {
        Constants.idProduct = product.getId();
        Log.d("Product Click", "Product ID: " + Constants.idProduct);

        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedProduct", product);

        DetailProductDialogFragment dialogFragment = new DetailProductDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getParentFragmentManager(), "Dialogshow");
    }
}