import { combineReducers } from "redux";

import shopReducer from "./Shopping/shopReducer";

const rootReducer = combineReducers({
  shop: shopReducer,
});

export default rootReducer;
