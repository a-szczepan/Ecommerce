import "../Cart/Cart.css";
import React, {useEffect, useState} from 'react';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import MuiDialogContent from '@material-ui/core/DialogContent';
import MuiDialogActions from '@material-ui/core/DialogActions';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import {useDispatch, useSelector} from "react-redux";
import {deleteFromCart, fetchProducts, quantityDown, quantityUp} from "../../redux/Shopping/shopping-actions";
import {Col, Row} from "react-bootstrap";
import Box from "@material-ui/core/Box";
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import RemoveCircleOutlineIcon from '@material-ui/icons/RemoveCircleOutline';
import DeleteIcon from '@material-ui/icons/Delete';


const styles = (theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(2),
  },
  closeButton: {
    position: 'absolute',
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[500],
  },
});

const DialogTitle = withStyles(styles)((props) => {
  const { children, classes, onClose, ...other } = props;
  return (
      <MuiDialogTitle disableTypography className={classes.root} {...other}>
        <Typography variant="h6">{children}</Typography>
        {onClose ? (
            <IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
              <CloseIcon />
            </IconButton>
        ) : null}
      </MuiDialogTitle>
  );
});

const DialogContent = withStyles((theme) => ({
  root: {
    padding: theme.spacing(2),
  },
}))(MuiDialogContent);

const DialogActions = withStyles((theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(1),
  },
}))(MuiDialogActions);

export function CustomizedDialogs() {
  const [open, setOpen] = useState(false);
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(()=>{
    dispatch(fetchProducts())

  },[])

  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  return (
      <div>
        <Button  onClick={handleClickOpen}>
          Koszyk
        </Button>
        <Dialog onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
          <DialogTitle id="customized-dialog-title" onClose={handleClose}>
            Koszyk
          </DialogTitle>
          <DialogContent dividers>
            <Paper style={{maxHeight: '80vh', minWidth:'30vw', overflow: 'auto', backgroundColor: 'inherit'}}
                   elevation={0}>
            <List>
              {post.shop.cart.map((x, index)=>
                  <ListItem key={index} className="cartListItem">
                    <Row>
                      <img height="100px" src={x.image}/>
                    </Row>
                    <Row>
                      <Typography variant="h6" style={{paddingLeft: "10px"}}>{x.name}</Typography>
                      <Box  style={{paddingLeft: "10px"}}>{new Intl.NumberFormat("pl-PL", {
                        style: "currency",
                        currency: "PLN",
                      }).format(x.price)}</Box>
                    </Row>
                      <Row className="quantity" style={{display: "flex", paddingLeft:"40px"}}>
                        <Button onClick={() => dispatch(quantityUp(x))}> <AddCircleOutlineIcon fontSize="small" /> </Button>
                        <Typography variant="h6" style={{padding: "0 10px 0 10px"}}>{x.quantity}</Typography>
                        <Button onClick={() => dispatch(quantityDown(x))}> <RemoveCircleOutlineIcon fontSize="small" /> </Button>
                        <Typography variant="h6">{new Intl.NumberFormat("pl-PL", {
                          style: "currency",
                          currency: "PLN",
                        }).format(x.price * x.quantity)}</Typography>
                        <Button onClick={()=>
                            dispatch(deleteFromCart(x.cart_id))
                        }> <DeleteIcon/> </Button>
                      </Row>
                  </ListItem>
              )}
            </List>
            </Paper>
            <Box fontSize="20px"> Suma: {post.shop.cartSum} </Box>
          </DialogContent>
          <DialogActions>
            <Button autoFocus onClick={handleClose} color="primary">
              Przejdź do płatności
            </Button>
          </DialogActions>
        </Dialog>
      </div>
  );
}