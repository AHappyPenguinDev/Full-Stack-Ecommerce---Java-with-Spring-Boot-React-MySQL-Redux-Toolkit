import { createTheme } from "@mui/material";

const customTheme = createTheme({
    palette: {
        mode: "light",
        background: {
            default: "#1e1e2e",
            paper: "282840",
        },
        primary: {
            main: "#00927c",
        },
        secondary: {
            main: "#EAF0F1",
        },
    },
});

export default customTheme;
