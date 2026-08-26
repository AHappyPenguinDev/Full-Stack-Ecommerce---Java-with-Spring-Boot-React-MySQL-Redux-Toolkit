import {
    Avatar,
    Box,
    Button,
    colors,
    IconButton,
    useMediaQuery,
    useTheme,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import {
    AddShoppingCart,
    Favorite,
    FavoriteBorder,
    Storefront,
} from "@mui/icons-material";

const Navbar = () => {
    const theme = useTheme();
    // in large screens this will be true, in small screens it be false
    const isLarge = useMediaQuery(theme.breakpoints.up("lg"));
    return (
        <Box>
            <div className="flex items-center justify-between px-5 lg:px-20 h-[70px] border-b">
                <div className="flex items-center gap-9">
                    <div className="flex items-center gap-2">
                    {!isLarge &&
                        <IconButton>
                            <MenuIcon></MenuIcon>
                        </IconButton>
                    }
                        <h1 className="logo cursor-pointer text-lg md:text-2xl text-primary-color">
                            Penguin Ecommerce
                        </h1>
                    </div>

                    <ul className="flex items-center font-medium text-gray-800">
                        {["Men", "Women", "Home & furniture", "Electronics"].map(
                            (item) => (
                                <li className="mainCategory hover:text-primary-color hover:border-b-2 h-[70px] px-4 border-primary-color flex items-center ">
                                    {item}
                                </li>
                            ),
                        )}
                    </ul>

                    <div className="flex gap-1 lg:gap-6 items-center">
                        <IconButton>
                            <SearchIcon />
                        </IconButton>
                        {true ? (
                            <Button className="flex  items-center gap-2">
                                <Avatar
                                    sx={{ width: 29, height: 29 }}
                                    src="https://cdn.creatureandcoagency.com/uploads/2014/10/emperor-penguin-facts-chicks.jpg"
                                /> 
                                <h1 className="font-semibold lg:block">Penguin</h1>
                            </Button>
                        ) : (
                            <Button variant="contained">Login</Button>
                        )}
                        <IconButton>
                            <FavoriteBorder className="text-gray-700" sx={{fontSize:29}}/>
                        </IconButton>
                        <IconButton>
                            <AddShoppingCart className="text-gray-700" sx={{fontSize:29}}/>
                        </IconButton>
                        {isLarge && (
                            <Button startIcon={<Storefront></Storefront>} variant="outlined">
                                Become a seller 
                            </Button>
                        )}
                    </div>
                </div>
            </div>
        </Box>
    );
};

export default Navbar;
