import {
  Heading,
  Avatar,
  Box,
  Center,
  Image,
  Flex,
  Text,
  Stack,
  useColorModeValue,
  Tag, Spacer, Button,
} from "@chakra-ui/react";
import AlertDialogDeleteCustomer from "./DeleteCustomer.jsx";
import CreateCustomerDrawer from "./CreateCustomerDrawer.jsx";

import UpdateCustomerForm from "./UpdateCustomerDrawer.jsx";
import UpdateCustomerDrawer from "./UpdateCustomerDrawer.jsx";

export default function CardWithImage({
  id,
  name,
  email,
  age,
  gender,
  imageNumber, fetchCustomers
}) {
  const randomUserGender = gender === "MALE" ? "men" : "women";

  return (
    <Center py={6}>
      <Box
        maxW={"300px"}
        minW={'300px'}
        w={"full"}
        m={2}
        p={2}
        bg={useColorModeValue("white", "gray.800")}
        boxShadow={"lg"}
        rounded={"md"}
        overflow={"hidden"}
      >
        <Image
          h={"120px"}
          w={"full"}
          src={
            "https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
          }
          objectFit={"cover"}
        />
        <Flex justify={"center"} mt={-12}>
          <Avatar
            size={"xl"}
            src={`https://randomuser.me/api/portraits/thumb/${randomUserGender}/${imageNumber}.jpg`}
            alt={"Author"}
            css={{
              border: "2px solid white",
            }}
          />
        </Flex>

        <Box p={6}>
          <Stack spacing={0} align={"center"} mb={5} textAlign={"center"}>
            <Tag borderRadius={"full"}>{id}</Tag>
            <Heading fontSize={"2xl"} fontWeight={500} fontFamily={"body"}>
              {name}
            </Heading>
            <Text color={"gray.500"}>{email}</Text>
            <Text color={"gray.500"}>
              Age {age} | {gender}
            </Text>
          </Stack>
        </Box>
        {/*<Box m={8}>
          <Flex>
            <UpdateCustomerForm
                id={id}
                name={name}
                email={email}
                age={age}
                gender={gender}
                fetchCustomers={fetchCustomers}
            />
            <Spacer/>
            <AlertDialogDeleteCustomer
                id={id}
                fetchCustomers={fetchCustomers}
                name={name}
            />
          </Flex>
        </Box>*/}
        <Stack direction={'row'} justify={'center'} spacing={6} p={4}>
          <Stack>
            <UpdateCustomerDrawer
                initialValues={{name, email, age}}
                customerId={id}
                fetchCustomers={fetchCustomers}
            />
          </Stack>
          <Stack>
            <AlertDialogDeleteCustomer
                id={id}
                fetchCustomers={fetchCustomers}
                name={name}
            />
          </Stack>
        </Stack>
      </Box>

    </Center>
  );
}
