import axios from "axios";

class Service {
  getAllProducts = async () => {
    return await axios.get(`http://localhost:9000/products`);
  };

  getWishlistByUser = async (user_id) => {
    return await axios.get(`http://localhost:9000/wishlist/user/${user_id}`);
  };

  addToWishlist = async (user_id, product_id) => {
    return await axios.post(`http://localhost:9000/wishlist/create-new`, {
      id: 0,
      user_id: user_id,
      product_id: product_id,
    });
  };
}

export default new Service();
