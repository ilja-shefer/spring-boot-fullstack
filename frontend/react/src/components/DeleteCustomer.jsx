import { useRef } from "react";
import {
  AlertDialog,
  AlertDialogBody,
  AlertDialogContent, AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogOverlay,
  Button, useDisclosure
} from "@chakra-ui/react";
import {deleCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";

function AlertDialogDeleteCustomer({id, fetchCustomers, name}) {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const cancelRef = useRef()

  return (
    <>
        <Button
            colorScheme={'red'}
            onClick={onOpen}
            bg={'red.400'}
            color={'white'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
            }}
            _focus={{
                bg: 'grey.500'
            }}
        >
            Delete
        </Button>

      <AlertDialog
        isOpen={isOpen}
        leastDestructiveRef={cancelRef}
        onClose={onClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize='lg' fontWeight='bold'>
              Delete Customer
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure you want to delete {name}? You can't undo this action afterwards.
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button colorScheme='red'
                      onClick={()=>{
                        deleCustomer(id)
                            .then((res) => {
                              successNotification(
                                  "Customer removed",
                                  `Customer with ${id} was successfully removed`
                              );
                              fetchCustomers();
                            })
                            .catch((err) => {
                              errorNotification(err.code, err.response.data.message);
                            })
                            .finally(() => {
                              onClose()
                            });
                      }} ml={3}>
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </>
  )
}

export default AlertDialogDeleteCustomer;