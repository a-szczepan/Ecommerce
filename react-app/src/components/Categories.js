import "../components/styles/Categories.css";
import { Row } from "react-bootstrap";
import React, { useEffect } from "react";
import Button from "@material-ui/core/Button";
import { useDispatch, useSelector } from "react-redux";
import Typography from "@material-ui/core/Typography";
import { setCurrentCategory } from "../redux/Shopping/shopping-actions";

const CategoryButton = (props) => {
  const dispatch = useDispatch();
  return (
    <Button onClick={() => dispatch(setCurrentCategory(props.catId))}>
      {props.category}
    </Button>
  );
};

const ResetButton = () => {
    const dispatch = useDispatch();
    return (
        <Button onClick={() => dispatch(setCurrentCategory("all"))}>
            Wszystkie kategorie
        </Button>
    );
};

export const Categories = () => {
  const post = useSelector((state) => state);

  return (
    <div>
      <Typography variant="h5">Kategorie</Typography>
      <ResetButton />
      <Row>
        {post.shop.categories.map((x) => (
          <CategoryButton catId={x.id} category={x.name} />
        ))}
      </Row>
    </div>
  );
};
