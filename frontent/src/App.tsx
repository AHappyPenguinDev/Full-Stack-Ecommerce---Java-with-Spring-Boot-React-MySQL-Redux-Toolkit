import { AddShoppingCart} from "@mui/icons-material";
import { Button, ThemeProvider } from "@mui/material";
import "./App.css";
import Navbar from "./customer/components/navbar/Navbar";
import Home from "./customer/pages/Home/Home";
import customTheme from "./Theme/customTheme";

function App() {
    return (
        <ThemeProvider theme={customTheme}>
            <div>
                <Navbar />
                <Home />
            </div>
        </ThemeProvider>
    );
}

export default App;
