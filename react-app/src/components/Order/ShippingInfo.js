import * as Yup from "yup";
import Typography from "@material-ui/core/Typography";
import {Field, Form, Formik} from "formik";
import Box from "@material-ui/core/Box";
import TextField from "@material-ui/core/TextField";
import WarningIcon from "@material-ui/icons/Warning";
import {Row} from "react-bootstrap";
import Button from "@material-ui/core/Button";
import {useDispatch} from "react-redux";
import {createShippingInfo, deleteShippingInfo} from "../../redux/Shopping/shopping-actions";
import EditIcon from '@material-ui/icons/Edit';


export const ShippingInfo = (props) => {
    const dispatch = useDispatch();

    const ShippingInfoSchema = Yup.object().shape({
        street_name: Yup.string().required("Pole nie może być puste"),
        building_number: Yup.string().required("Pole nie może być puste"),
        postal_code: Yup.string().required("Pole nie może być puste"),
        city: Yup.string().required("Pole nie może być puste")
    });

    const onContinueClick = async (values, actions) => {
        try {
            dispatch(createShippingInfo(props.user,values.street_name,values.building_number, values.postal_code, values.city))
        } catch (error) {

        }
    };

    if (props.shipping === "create") {
        return (
            <>
                <Typography variant="h5" style={{paddingTop:"2%"}}>Dane do wysyłki:</Typography>
                <Formik
                    initialValues={{
                        street_name: "",
                        building_number: "",
                        postal_code: "",
                        city: ""
                    }}
                    validationSchema={ShippingInfoSchema}
                    onSubmit={(values, actions) => onContinueClick(values, actions)}
                >
                    {({ errors, touched, values, isSubmitting }) => (
                        <Form>
                            <Box fontSize={17} fontWeight={500} style={{ paddingTop: "2%" }}>
                                Ulica:
                            </Box>
                            <Field
                                as={TextField}
                                name="street_name"
                                autoComplete="off"
                                style={{ minWidth: "300px" }}
                                required
                            />
                            {errors.street_name && touched.street_name ? (
                                <div
                                    style={{
                                        display: "flex",
                                        padding: "15px 0 0 0",
                                        fontSize: "12px",
                                    }}
                                >
                                    <WarningIcon style={{ color: "#ecb544" }} fontSize="small" />
                                    <Box style={{ paddingLeft: "10px" }}>{errors.street_name}</Box>
                                </div>
                            ) : null}
                            <Box fontSize={17} fontWeight={500} style={{ paddingTop: "2%" }}>
                                Numer budynku / mieszkania:
                            </Box>
                            <Field
                                as={TextField}
                                name="building_number"
                                autoComplete="off"
                                style={{ minWidth: "300px" }}
                                required
                            />
                            {errors.building_number && touched.building_number ? (
                                <div
                                    style={{
                                        display: "flex",
                                        padding: "15px 0 0 0",
                                        fontSize: "12px",
                                    }}
                                >
                                    <WarningIcon style={{ color: "#ecb544" }} fontSize="small" />
                                    <Box style={{ paddingLeft: "10px" }}>{errors.building_number}</Box>
                                </div>
                            ) : null}
                            <Box fontSize={17} fontWeight={500} style={{ paddingTop: "2%" }}>
                                Kod pocztowy:
                            </Box>
                            <Field
                                as={TextField}
                                name="postal_code"
                                autoComplete="off"
                                style={{ minWidth: "300px" }}
                                required
                            />
                            {errors.postal_code && touched.postal_code ? (
                                <div
                                    style={{
                                        display: "flex",
                                        padding: "15px 0 0 0",
                                        fontSize: "12px",
                                    }}
                                >
                                    <WarningIcon style={{ color: "#ecb544" }} fontSize="small" />
                                    <Box style={{ paddingLeft: "10px" }}>{errors.postal_code}</Box>
                                </div>
                            ) : null}
                            <Box fontSize={17} fontWeight={500} style={{ paddingTop: "2%" }}>
                                Miasto:
                            </Box>
                            <Field
                                as={TextField}
                                name="city"
                                autoComplete="off"
                                style={{ minWidth: "300px" }}
                                required
                            />
                            {errors.city && touched.city ? (
                                <div
                                    style={{
                                        display: "flex",
                                        padding: "15px 0 0 0",
                                        fontSize: "12px",
                                    }}
                                >
                                    <WarningIcon style={{ color: "#ecb544" }} fontSize="small" />
                                    <Box style={{ paddingLeft: "10px" }}>{errors.city}</Box>
                                </div>
                            ) : null}
                            <Row style={{ display: "flex", padding:"1% 0 0 240px"}}>
                                <Field as={Button} type="submit">Potwierdź</Field>
                            </Row>
                        </Form>
                    )}
                </Formik>
            </>
        );
    } else {
        return (
            <>
                <Typography variant="h5" style={{paddingTop:"2%"}}>Dane do wysyłki:</Typography>
                <Row>
                    <Box fontSize={16} fontWeight={600} style={{ paddingTop: "1%" }}>
                        Ulica:
                    </Box>
                    {props.shipping.street_name}
                </Row>
                <Row>
                    <Box fontSize={16} fontWeight={600} style={{ paddingTop: "1%" }}>
                        Numer budynku/mieszkania:
                    </Box>
                    {props.shipping.building_number}
                </Row>
                <Row>
                    <Box fontSize={16} fontWeight={600} style={{ paddingTop: "1%" }}>
                        Kod pocztowy:
                    </Box>
                    {props.shipping.postal_code}
                </Row>
                <Row>
                    <Box fontSize={16} fontWeight={600} style={{ paddingTop: "1%" }}>
                        Miasto:
                    </Box>
                    {props.shipping.city}
                </Row>

                <Row style={{ display: "flex", padding:"1% 0 0 240px"}}>
                    <Button onClick={()=> dispatch(deleteShippingInfo(props.shipping.id))}><EditIcon style={{paddingRight:"10px"}}/>ZMIEŃ</Button>
                </Row>
            </>
        );
    }
};