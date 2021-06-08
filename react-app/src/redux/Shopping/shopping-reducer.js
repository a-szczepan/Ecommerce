import * as actionTypes from "./shopping-types";

const INITIAL_STATE = {
  products: [],
  wishlist: [],
  wishlistProducts: [],
  cart: [],
  cartSum: 0,
  loading: false,
};

const shopReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case actionTypes.LOAD_PRODUCTS:
      return {
        ...state,
        loading: false,
        wishlist: state.wishlist,
        products: action.payload,
        wishlistProducts: state.wishlistProducts,
      };

    case actionTypes.LOAD_CART:
      const cart = []
      const allProducts = state.products
        allProducts.map(product => {
          action.payload.map( cart_item => {
                if (product.id === cart_item.product_id){
                    let newProd = product
                    newProd["quantity"] = cart_item.quantity
                    newProd["cart_id"] = cart_item.id
                    newProd["user_id"] = cart_item.user_id
                    cart.push(newProd)
                }
              }
          )
        })
      let sumAfterLoad = 0
      cart.map(product => sumAfterLoad += parseFloat(product.price))
      sumAfterLoad = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(sumAfterLoad)
      return {
        ...state,
        loading: false,
        cart: cart,
        cartSum: sumAfterLoad
      };

    case actionTypes.ADD_TO_CART:
      let product = state.products.filter(product=> product.id === action.payload.product_id)
        product=product[0]
        product["quantity"] = action.payload.quantity
        product["cart_id"] = action.payload.id
        product["user_id"] = action.payload.user_id
      state.cart.push(product);
      let sum = 0
        state.cart.map(product => sum += parseFloat(product.price))
      sum= new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(sum)
      return {
        ...state,
        loading: false,
        cart: state.cart,
        cartSum: sum
      };
    case actionTypes.REMOVE_FROM_CART:
      let newCart = []
        if(state.cart.length > 1 ){
          newCart = state.cart.filter(product => product.id !== action.payload)
        } else {
          newCart = []
        }
      let newSum = 0
      newCart.map(product => newSum += parseFloat(product.price))
        newSum = new Intl.NumberFormat("pl-PL", {
          style: "currency",
          currency: "PLN",
        }).format(newSum)
      return {
        ...state,
        loading: false,
        cart: newCart,
        cartSum: newSum
      };

    case actionTypes.LOAD_WISHLIST:
      return {
        ...state,
        loading: false,
        wishlist: action.payload,
        products: state.products,
        wishlistProducts: state.wishlistProducts,
      };
    case actionTypes.LOAD_WISHLIST_PRODUCTS:
      return {
        ...state,
        loading: false,
        wishlistProducts: action.payload,
      };
    case actionTypes.REMOVE_FROM_WISHLIST:
      let newWishlistProducts = [];
      if (state.wishlistProducts.length > 0) {
        newWishlistProducts = state.wishlistProducts.filter(
          ({ id }) => id !== action.payload
        );
      } else {
        newWishlistProducts = [];
      }
      return {
        ...state,
        loading: false,
        wishlistProducts: newWishlistProducts,
      };
    case actionTypes.ADD_TO_WISHLIST:
      state.wishlist.push(action.payload);
      return {
        ...state,
        loading: false,
        wishlist: state.wishlist,
      };
    default:
      return state;
  }
};

export default shopReducer;
