import React from 'react';
import { View, FlatList, Text, StyleSheet } from 'react-native';
import { net, oauth, smartstore } from 'react-native-force';

interface Response {
    records: Record[]
}

interface Record {
    Id: String,
    Name: String
}

interface Props {
}

interface State {
    data: Record[]
}

class AccountListPage extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = { data: [] };
    }

    componentDidMount() {
        var that = this;
        oauth.getAuthCredentials(
            (user) => {
                console.log(user);
                // smartstore.registerSoup();
                that.fetchData();
            },
            () => {
                oauth.authenticate(
                    () => that.fetchData(),
                    (error) => console.log('Failed to authenticate:' + error)
                );
            });
    }

    fetchData() {
        var that = this;
        net.query('SELECT Id, Name FROM Account LIMIT 100',
            (response: Response) => that.setState({ data: response.records }),
            (error) => console.log('Failed to query:' + error)
        );
    }

    render() {
        return (
            <View style={styles.container}>
                <FlatList
                    data={this.state.data}
                    renderItem={({ item }) => <Text style={styles.item}>{item.Name}</Text>}
                    keyExtractor={(item, index) => 'key_' + index}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingTop: 22,
        backgroundColor: 'white',
    },
    item: {
        padding: 10,
        fontSize: 18,
        height: 44,
    }
});

export default AccountListPage;