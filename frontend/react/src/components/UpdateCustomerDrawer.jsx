import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";

const CloseIcon = () => "X";
const UpdateCustomerDrawer = ({fetchCustomers, initialValues, customerId}) => {
    const {isOpen, onOpen, onClose} = useDisclosure();

    return (
        <>
            <Button
                onClick={onOpen}
                bg={'gray.200'}
                color={'black'}
                rounded={'full'}
                _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                }}
            >
                Update
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay/>
                <DrawerContent>
                    <DrawerCloseButton/>
                    <DrawerHeader>Update customer</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm
                            fetchCustomers={fetchCustomers}
                            customerId={customerId}
                            initialValues={initialValues}
                            onClose={onClose}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button
                            leftIcon={<CloseIcon/>}
                            colorScheme={"teal"}
                            onClick={onClose}
                        >
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    );
};
export default UpdateCustomerDrawer;