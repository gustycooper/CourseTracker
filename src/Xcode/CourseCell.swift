//
//  CourseCell.swift
//  CourseTracker
//
//  Created by Diego  Bustamante on 10/24/17.
//  Copyright © 2017 Diego Bustamante. All rights reserved.
//

import UIKit

class CourseCell: UITableViewCell {

    @IBOutlet weak var Title: UILabel!
    @IBOutlet weak var cid: UILabel!
    @IBOutlet weak var coursename: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
