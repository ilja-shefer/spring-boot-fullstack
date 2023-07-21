const UserProfile = ({age, name, gender, imageNumber, ...props}) => {
    gender = gender === "FEMALE" ? "women" : "men"
    return (
        <div>
            <h1>{name}</h1>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`} alt={""}/>
            {props.children}
        </div>
    );
}

export default UserProfile;