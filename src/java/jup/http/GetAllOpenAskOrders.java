/*
 * Copyright © 2013-2016 The Nxt Core Developers.
 * Copyright © 2016-2017 Jelurida IP B.V.
 * Copyright © 2017-2020 Sigwo Technologies
 * Copyright © 2020-2021 Jupiter Project Developers
 *
 * See the LICENSE.txt file at the top-level directory of this distribution
 * for licensing information.
 *
 * Unless otherwise agreed in a custom licensing agreement with Jelurida B.V.,
 * no part of the Nxt software, including this file, may be copied, modified,
 * propagated, or distributed except according to the terms contained in the
 * LICENSE.txt file.
 *
 * Removal or modification of this copyright notice is prohibited.
 *
 */

package jup.http;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import jup.Order;
import jup.db.DbIterator;

public final class GetAllOpenAskOrders extends APIServlet.APIRequestHandler {

    static final GetAllOpenAskOrders instance = new GetAllOpenAskOrders();
    
    private GetAllOpenAskOrders() {
        super(new APITag[] {APITag.AE}, "firstIndex", "lastIndex", "includeNTFInfo");
    }
    

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) {
        JSONObject response = new JSONObject();
        JSONArray ordersData = new JSONArray();

        int firstIndex = ParameterParser.getFirstIndex(req);
        int lastIndex = ParameterParser.getLastIndex(req);
        boolean includeNTFInfo = "true".equalsIgnoreCase(req.getParameter("includeNTFInfo"));

        try (DbIterator<Order.Ask> askOrders = Order.Ask.getAll(firstIndex, lastIndex)) {
            while (askOrders.hasNext()) {
            	ordersData.add(JSONData.askOrder(askOrders.next(), includeNTFInfo));
            }
        }

        response.put("openOrders", ordersData);
        return response;
    }
}