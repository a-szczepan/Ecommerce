import "../components/styles/Product.css";
import { Col } from "react-bootstrap";
import { Component } from "react";
import Button from "@material-ui/core/Button";
import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Typography from "@material-ui/core/Typography";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import Service from "../services/service";
import { connect } from "react-redux";
import { getProducts } from "../store/actions/productsActions";

const useStyles = makeStyles({
  root: {
    maxWidth: 345,
  },
});

function ImgMediaCard(props) {
  const [products, setProducts] = React.useState();

  const addProductToWishlist = async (product) => {
    const res = await Service.addToWishlist(1, product.id);
  };

  const classes = useStyles();
  return (
    <Card className={classes.root}>
      <CardActionArea>
        <CardMedia
          component="img"
          alt="product"
          height="320"
          image={props.product.image}
          title={props.product.name}
        />
        <CardContent
          style={{
            overflow: "hidden",
            textOverflow: "ellipsis",
            width: "19rem",
          }}
        >
          <Typography noWrap gutterBottom variant="h5" component="h2">
            {props.product.name}
          </Typography>
          <Typography>
            {new Intl.NumberFormat("pl-PL", {
              style: "currency",
              currency: "PLN",
            }).format(props.product.price)}
          </Typography>
        </CardContent>
      </CardActionArea>
      <CardActions>
        <Button size="small" style={{ color: "#212529" }}>
          <FavoriteBorderIcon
            onClick={(e) => {
              e.preventDefault();
              addProductToWishlist(props.product);
            }}
          />
        </Button>
        <Button size="small" style={{ color: "#212529" }}>
          Do koszyka
        </Button>
      </CardActions>
    </Card>
  );
}

class Products extends Component {
  componentDidMount() {
    this.props.getProducts();
  }
  render() {
    const { products } = this.props.products;
    console.log(this.props.products);

    return (
      <>
        {products.map((u, index) => (
          <Col key={index} className="product">
            <ImgMediaCard key={index} product={u} />
          </Col>
        ))}
      </>
    );
  }
}

const mapStateToProps = (state) => ({ products: state.products });

export default connect(mapStateToProps, { getProducts })(Products);
