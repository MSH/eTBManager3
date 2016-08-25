
function model() {
    return {
        fields: {
            releaseDate: {
                required: function() { return !!this.collectionDate; },
                validators: {
                    v0: function() { return this.collectionDate > this.releaseDate; },
                    v1: function() {
                        this.value += 1;
                        return this.value;
                    }
                }
            }
        },
        validators: {
            v0: function() { return this.birthDate < new Date(); }
        }
    }
}
