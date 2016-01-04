package com.lianmeng.core.framework.service;

import java.util.List;

import com.lianmeng.core.product.vo.ProdcutHistory;


public interface IECManager {
	 void addProductToHistory(ProdcutHistory history);
	 void clearProductHistory();
	 List<ProdcutHistory> getAllProductHistory();
}
