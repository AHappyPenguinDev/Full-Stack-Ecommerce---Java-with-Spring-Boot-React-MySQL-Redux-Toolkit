import penguinSweater from '../../../../assets/penguinInSweater.avif'

const CategoryGrid = () => {
    return (
        <div className="grid gap-4 grid-rows-12 grid-cols-12 lg:h-[600px] px-5 lg:px-20">
            <div className="col-span-3 row-span-12 text-white">
                <img src={penguinSweater}/> 
            </div>
            <div className="col-span-3 row-span-6 text-white">
                <img src={penguinSweater}/> 
            </div>
        </div>
    );
};

export default CategoryGrid;
