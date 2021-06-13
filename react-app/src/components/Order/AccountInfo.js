import * as Yup from "yup";
import Typography from "@material-ui/core/Typography";
import { Field, Form, Formik } from "formik";
import Box from "@material-ui/core/Box";
import TextField from "@material-ui/core/TextField";
import WarningIcon from "@material-ui/icons/Warning";
import { Row } from "react-bootstrap";
import Button from "@material-ui/core/Button";
import { useDispatch } from "react-redux";
import {
  createAccountInfo,
  deleteAccountInfo,
  deleteShippingInfo,
} from "../../redux/Shopping/shopping-actions";
import EditIcon from "@material-ui/icons/Edit";

export const AccountInfo = (props) => {
  const dispatch = useDispatch();

  const AccountInfoSchema = Yup.object().shape({
    firstName: Yup.string()
      .min(2, "Wprowadzona wartość jest za krótka")
      .max(32, "Przekroczono maksymalny zakres 32 znaków")
      .required("Pole nie może być puste"),
    lastName: Yup.string()
      .min(2, "Wprowadzona wartość jest za krótka")
      .max(32, "Przekroczono maksymalny zakres 32 znaków")
      .required("Pole nie może być puste"),
  });

  const onContinueClick = async (values, actions) => {
    try {
      dispatch(
        createAccountInfo(props.user, values.firstName, values.lastName)
      );
    } catch (error) {}
  };

  if (props.account === "create") {
    return (
      <>
        <Typography variant="h5">Twoje dane:</Typography>
        <Formik
          initialValues={{
            firstName: "",
            lastName: "",
          }}
          validationSchema={AccountInfoSchema}
          onSubmit={(values, actions) => onContinueClick(values, actions)}
        >
          {({ errors, touched, values, isSubmitting }) => (
            <Form>
              <Box fontSize={17} fontWeight={500} style={{ paddingTop: "2%" }}>
                Imię:{" "}
              </Box>
              <Field
                as={TextField}
                name="firstName"
                autoComplete="off"
                style={{ minWidth: "300px" }}
                required
              />
              {errors.firstName && touched.firstName ? (
                <div
                  style={{
                    display: "flex",
                    padding: "15px 0 0 0",
                    fontSize: "12px",
                  }}
                >
                  <WarningIcon style={{ color: "#ecb544" }} fontSize="small" />
                  <Box style={{ paddingLeft: "10px" }}>{errors.firstName}</Box>
                </div>
              ) : null}
              <Box fontSize={17} fontWeight={500} style={{ paddingTop: "2%" }}>
                Nazwisko:{" "}
              </Box>
              <Field
                as={TextField}
                name="lastName"
                autoComplete="off"
                style={{ minWidth: "300px" }}
                required
              />
              {errors.lastName && touched.lastName ? (
                <div
                  style={{
                    display: "flex",
                    padding: "15px 0 0 0",
                    fontSize: "12px",
                  }}
                >
                  <WarningIcon style={{ color: "#ecb544" }} fontSize="small" />
                  <Box style={{ paddingLeft: "10px" }}>{errors.lastName}</Box>
                </div>
              ) : null}
              <Row style={{ display: "flex", padding: "1% 0 0 240px" }}>
                <Field as={Button} type="submit">
                  Potwierdź
                </Field>
              </Row>
            </Form>
          )}
        </Formik>
      </>
    );
  } else {
    return (
      <>
        <Typography variant="h5" style={{ paddingTop: "2%" }}>
          Twoje dane:
        </Typography>
        <Row>
          <Box fontSize={16} fontWeight={600} style={{ paddingTop: "1%" }}>
            Imię:
          </Box>
          {props.account.first_name}
        </Row>
        <Row>
          <Box fontSize={16} fontWeight={600} style={{ paddingTop: "1%" }}>
            Nazwisko:
          </Box>
          {props.account.last_name}
        </Row>
        <Row style={{ display: "flex", padding: "1% 0 0 240px" }}>
          <Button onClick={() => dispatch(deleteAccountInfo(props.account.id))}>
            <EditIcon style={{ paddingRight: "10px" }} />
            ZMIEŃ
          </Button>
        </Row>
      </>
    );
  }
};
